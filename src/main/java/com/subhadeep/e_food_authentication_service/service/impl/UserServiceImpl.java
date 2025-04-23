package com.subhadeep.e_food_authentication_service.service.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.subhadeep.e_food_authentication_service.exceptions.UnauthorizeException;
import com.subhadeep.e_food_authentication_service.model.RefreshTokenEO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.subhadeep.e_food_authentication_service.constant.RegexConstant;
import com.subhadeep.e_food_authentication_service.constant.RoleConstants;
import com.subhadeep.e_food_authentication_service.dto.PaginatedResponse;
import com.subhadeep.e_food_authentication_service.dto.UserDTO;
import com.subhadeep.e_food_authentication_service.exceptions.InvalidCredException;
import com.subhadeep.e_food_authentication_service.exceptions.InvalidRequestException;
import com.subhadeep.e_food_authentication_service.exceptions.ResourceNotFoundException;
import com.subhadeep.e_food_authentication_service.model.RoleEO;
import com.subhadeep.e_food_authentication_service.model.UserEO;
import com.subhadeep.e_food_authentication_service.repo.RoleRepo;
import com.subhadeep.e_food_authentication_service.repo.UserRepo;
import com.subhadeep.e_food_authentication_service.security.AuthUser;
import com.subhadeep.e_food_authentication_service.security.AuthenticationService;
import com.subhadeep.e_food_authentication_service.security.JwtUtils;
import com.subhadeep.e_food_authentication_service.service.FileUploadserService;
import com.subhadeep.e_food_authentication_service.service.RefreshTokenService;
import com.subhadeep.e_food_authentication_service.service.UserService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Validated
public class UserServiceImpl implements UserService {

    @Value("${folder.name.PROFILE_PHOTO_IMAGE_FOLDER}")
    private String profilePhotoFolder;

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final RoleRepo roleRepo;
    private final RefreshTokenService refreshTokenService;
    private final JwtUtils jwtUtils;
    private final AuthenticationService authenticationService;
    private final FileUploadserService fileUploadserService;

    @Override
    public Map<?, ?> registerAsCustomer(@Valid UserDTO data, MultipartFile multipartFile) {

        UserEO fetchedUserFromEmail = userRepo.findByEmailIgnoreCase(data.getEmail());
        UserEO fetchedUserFromMobileNo = userRepo.findByMobileNo(data.getMobileNo());

        if (fetchedUserFromEmail != null) {
            throw new InvalidRequestException("Email has been used already");
        }

        if (fetchedUserFromMobileNo != null) {
            throw new InvalidRequestException("Mobile Number has been used already");
        }

        String photoUrl = null;

        if (multipartFile != null) {
            try {
                photoUrl = fileUploadserService.uploadFile(multipartFile, profilePhotoFolder);
            } catch (Exception e) {
            }
        }

        UserEO goingTosave = modelMapper.map(data, UserEO.class);

        if (photoUrl != null) {
            goingTosave.setProfilePhoto(photoUrl);
        }

        RoleEO userRole = roleRepo.findById(RoleConstants.USER_ROLE_ID)
                .orElseThrow(() -> new ResourceNotFoundException("ROLE", "role", "USER"));

        goingTosave.setPassword(passwordEncoder.encode(data.getPassword()));
        Set<RoleEO> roles = new HashSet<>();
        roles.add(userRole);
        goingTosave.setRoles(roles);

        UserEO saved = userRepo.save(goingTosave);

        String refreshToken = refreshTokenService.generateRefreshToken(saved.getId());
        String jwtToken = jwtUtils.generateToken(AuthUser.builder()
                .userEO(saved)
                .build());

        Cookie cookieJwt = new Cookie("jwtToken", jwtToken);
        cookieJwt.setSecure(true);
        cookieJwt.setHttpOnly(true);

        Cookie cookieRe = new Cookie("refreshToken", refreshToken);
        cookieRe.setHttpOnly(true);
        cookieRe.setSecure(true);

        HttpServletResponse response = getCurrentResponse();
        response.addCookie(cookieJwt);
        response.addCookie(cookieRe);

        return Map.of("msg", "Registration success");

    }

    @Override
    public UserDTO getSingleUser(Integer userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getSingleUser'");
    }

    @Override
    public Map<?, ?> deleteUser(Integer userId) {

        UserEO fetchedUser = userRepo.findById(userId)
                .orElseThrow(()-> new ResourceNotFoundException("User","ID",userId.toString()));

        userRepo.delete(fetchedUser);
        refreshTokenService.getAllUserTokens(userId);

        return Map.of("msg","User Deleted");
    }

    @Override
    public PaginatedResponse getAllUsers(Integer pageNo, Integer pageSize, String sortBy, String sortDir) {

        Sort sort = Sort.by(Direction.fromString(sortDir), sortBy);
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<UserEO> page = userRepo.findAll(pageable);

        List<UserDTO> userList = page.getContent()
                .stream()
                .map(e -> modelMapper.map(e, UserDTO.class))
                .toList();

        return PaginatedResponse.builder()
                .content(userList)
                .currentPage(page.getNumber())
                .isLastPage(page.isLast())
                .totalPages(page.getTotalPages())
                .totalItems((int) page.getTotalElements())
                .currentPageItemsNumber(page.getNumberOfElements())
                .pageSize(page.getSize()).build();

    }

    @Override
    public Map<String, String> loginUser(Map<String, String> loginRequest) {

        String userName = loginRequest.get("userName");
        String password = loginRequest.get("password");

        if (userName == null) {
            throw new InvalidRequestException("Email or Mobile Number is Required");
        }

        if (password == null) {
            throw new InvalidRequestException("Password is Required");
        }

        boolean authenticated = authenticationService.doAuthenticate(userName, password);

        if (!authenticated) {
            throw new InvalidCredException("Username or Password is Incorrect");
        }

        UserEO userEO = null;

        boolean isEmail = userName.matches(RegexConstant.EMAIL_REGEX);
        userEO = isEmail ? userRepo.findByEmailIgnoreCase(userName)
                : userRepo.findByMobileNo(userName);

        String jwtToken = jwtUtils.generateToken(new AuthUser(userEO));
        String refreshToken = refreshTokenService.generateRefreshToken(userEO.getId());

        Cookie cookieJwt = new Cookie("jwtToken", jwtToken);
        cookieJwt.setSecure(true);
        cookieJwt.setHttpOnly(true);

        Cookie cookieRe = new Cookie("refreshToken", refreshToken);
        cookieRe.setHttpOnly(true);
        cookieRe.setSecure(true);

        HttpServletResponse response = getCurrentResponse();
        response.addCookie(cookieJwt);
        response.addCookie(cookieRe);

        return Map.of("msg", "Login success");

    }

    public HttpServletResponse getCurrentResponse() {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attrs != null ? attrs.getResponse() : null;
    }

    @Override
    public UserDTO getCurrentUser() {
        UserEO currentUser = authenticationService.getCurrentUserObject();
        return modelMapper.map(currentUser,UserDTO.class);
    }

    @Override
    public Map<?, ?> userDeletedBySelf() {
         UserEO currentUser = authenticationService.getCurrentUserObject();

         Integer userId = currentUser.getId();
         if(currentUser == null){
             throw new UnauthorizeException("Invalid token");
         }
             userRepo.delete(currentUser);
             refreshTokenService.deleteAllUserRefreshToken(userId);

             return Map.of("msg","User deleted");

    }

    @Override
    public Map<?, ?> doLogOut(String authHeader) {
        String token = authHeader.substring(7);

        try {
            jwtUtils.blacklistToken(token);
            return Map.of("msg","Logged out");
        } catch (Exception e) {

        }
        throw new UnauthorizeException("Invalid Token");
    }

}
