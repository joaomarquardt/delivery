package com.api.delivery.mappers;

import com.api.delivery.domain.Product;
import com.api.delivery.dtos.requests.CreateProductRequest;
import com.api.delivery.dtos.requests.UpdateProductRequest;
import com.api.delivery.dtos.responses.ProductResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    Product toProductEntity(CreateProductRequest request);

    ProductResponse toProductResponse(Product product);

    List<ProductResponse> toProductResponseList(List<Product> products);

    @Mapping(target = "id", ignore = true)
    void updateProductFromRequest(UpdateProductRequest request, @MappingTarget Product product);
}
