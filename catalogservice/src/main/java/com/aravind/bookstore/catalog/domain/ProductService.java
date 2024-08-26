package com.aravind.bookstore.catalog.domain;

import com.aravind.bookstore.catalog.ApplicationProperties;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;

    private final ApplicationProperties applicationProperties;

    ProductService(ProductRepository productRepository, ApplicationProperties applicationProperties) {
        this.productRepository = productRepository;
        this.applicationProperties = applicationProperties;
    }

    public PagedResult<Product> getProducts(int pageNo) {
        Sort sort = Sort.by("name").ascending();
        pageNo = pageNo <= 1 ? 0 : pageNo - 1;
        PageRequest pageable = PageRequest.of(pageNo, applicationProperties.pageSize(), sort);

        Page<Product> produtcsPage = productRepository.findAll(pageable).map(ProductMapper::toProduct);

        return new PagedResult<>(
                produtcsPage.getContent(),
                produtcsPage.getTotalElements(),
                produtcsPage.getNumber() + 1,
                produtcsPage.getTotalPages(),
                produtcsPage.isFirst(),
                produtcsPage.isLast(),
                produtcsPage.hasNext(),
                produtcsPage.hasPrevious());
    }

    public Optional<Product> getProductByCode(String code) {
        return productRepository.findByCode(code).map(ProductMapper::toProduct);
    }
}
