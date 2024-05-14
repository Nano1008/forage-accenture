package com.mockcompany.webapp.service;

import com.mockcompany.webapp.data.ProductItemRepository;
import com.mockcompany.webapp.model.ProductItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Collection;

@Service
public class SearchService {
    private final ProductItemRepository productItemRepository;

    @Autowired
    public SearchService(ProductItemRepository productItemRepository) {
        this.productItemRepository = productItemRepository;
    }

    public Collection<ProductItem> search(String query) {
        // case-insensitive search
        String lowerCaseQuery = query.toLowerCase();

        Iterable<ProductItem> allItems = this.productItemRepository.findAll();
        List<ProductItem> itemList = new ArrayList<>();

        for (ProductItem item : allItems) {
            String lowerCaseName = item.getName().toLowerCase();
            String lowerCaseDescription = item.getDescription().toLowerCase();

            // Check if the query is quoted
            boolean quoted = query.startsWith("\"") && query.endsWith("\"");

            /* If the query is quoted, check if the name or description is an exact match.
             * If the query is not quoted, check if the name or description contains the query.
             */
            if (quoted) {
                // Remove the quotes from the query
                String queryWithoutQuotes = lowerCaseQuery.replace("\"", "");
                if (lowerCaseName.equals(queryWithoutQuotes) || lowerCaseDescription.equals(queryWithoutQuotes)) {
                    itemList.add(item);
                }
            }
            else {
                if (lowerCaseName.contains(lowerCaseQuery) || lowerCaseDescription.contains(lowerCaseQuery)) {
                    itemList.add(item);
                }
            }
        }
        return itemList;
    }
}
