package com.kamranali.bookcatalogservice.resources;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import com.kamranali.bookcatalogservice.models.CatalogItem;
import com.kamranali.bookcatalogservice.models.Book;
import com.kamranali.bookcatalogservice.models.Rating;
import com.kamranali.bookcatalogservice.models.UserRating;

@RestController
@RequestMapping("/catalog")
public class BookCatalogResource {
   @Autowired
    private RestTemplate restTemplate;
   @Autowired
   private DiscoveryClient discoveryClient;

    @RequestMapping("/{userId}")
    public List<CatalogItem> getBookCatalog(@PathVariable("userId")String userId) {

        UserRating ratings = restTemplate.getForObject("http://book-rating-service/ratingsdata/users/"+userId, UserRating.class);

                return ratings.getUserRating().stream().map(rating -> {

            Book book = restTemplate.getForObject("http://book-info-service/books/"+rating.getBookId(), Book.class);
            return new CatalogItem(book.getName(), "Very good and emotional book", rating.getRating());

        }
        )
                .collect(Collectors.toList());
    }

}

