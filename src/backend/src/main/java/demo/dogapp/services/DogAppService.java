package demo.dogapp.services;

import demo.dogapp.dtos.DogBreedDto;
import demo.dogapp.exceptions.InvalidPageNumberException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class DogAppService {

    private final DogCEOService dogCEOService;

    private static final String PAGED_DATA_KEY = "dogBreeds";
    private static final String CURRENT_PAGE_KEY = "currentPage";
    private static final String TOTAL_ITEMS_KEY = "totalItems";
    private static final String TOTAL_PAGES_KEY = "totalPages";

    public DogAppService(DogCEOService dogCEOService) {
        this.dogCEOService = dogCEOService;
    }

    public Map<String, Object> getPaginatedData(final int pageNumber, final int pageSize) {
        Map<String, Object> response = new HashMap<>();
        List<String> allBreeds = this.dogCEOService.getAllBreeds();
        try {
            if (pageNumber < 0) {
                throw new InvalidPageNumberException("Page number must be greater than zero", new Throwable());
            }
            if (!allBreeds.isEmpty()) {
                int startIndex = pageNumber * pageSize;
                int endIndex = startIndex + pageSize;
                if (pageSize > allBreeds.size() || endIndex > allBreeds.size()) {
                    endIndex = allBreeds.size();
                }
                List<String> pagedData = allBreeds.subList(startIndex, endIndex);
                this.cacheDogBreedDetailForPaginatedData(pagedData);

                response.put(PAGED_DATA_KEY, pagedData);
                response.put(CURRENT_PAGE_KEY, pageNumber);
                response.put(TOTAL_ITEMS_KEY, allBreeds.size());

                double totalPages = allBreeds.size() / pageSize;
                if (allBreeds.size() % pageSize != 0) {
                    totalPages++;
                }
                response.put(TOTAL_PAGES_KEY, (int) totalPages);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
        return response;
    }

    public Optional<DogBreedDto> getDogBreedById(final String id) {
        return this.dogCEOService.getBreedByName(id);
    }

    private void cacheDogBreedDetailForPaginatedData(final List<String> breeds) {
        if (breeds != null) {
            breeds.forEach(this.dogCEOService::getBreedByName);
        }
    }
}
