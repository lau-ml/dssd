package dssd.server.DTO;

import java.util.List;

import lombok.Getter;

@Getter
public class PaginatedResponseDTO<T> {
    private List<T> content;
    private int totalPages;
    private long totalElements;
    private int page;
    private int size;

    public PaginatedResponseDTO() {
    }

    public PaginatedResponseDTO(List<T> content, int totalPages, long totalElements, int page, int size) {
        this.content = content;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.page = page;
        this.size = size;
    }

}