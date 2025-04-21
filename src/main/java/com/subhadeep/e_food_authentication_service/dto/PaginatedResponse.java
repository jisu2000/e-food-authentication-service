package com.subhadeep.e_food_authentication_service.dto;

import java.util.List;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class PaginatedResponse {
    private List<?> content;
    private Integer currentPage;
    private Integer pageSize;
    private Integer totalItems;
    private Integer currentPageItemsNumber;
    private Integer totalPages;
    private boolean isLastPage;
}
