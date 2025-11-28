package com.api.delivery.mappers;

import com.api.delivery.domain.ProductType;
import com.api.delivery.dtos.requests.CreateProductTypeRequest;
import com.api.delivery.dtos.requests.UpdateProductTypeRequest;
import com.api.delivery.dtos.responses.ProductTypeResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductTypeMapper {
    ProductTypeMapper INSTANCE = Mappers.getMapper(ProductTypeMapper.class);

    ProductType toProductTypeEntity(CreateProductTypeRequest request);

    ProductTypeResponse toProductTypeResponse(ProductType productType);

    List<ProductTypeResponse> toProductTypeResponseList(List<ProductType> productTypes);

    @Mapping(target = "id", ignore = true)
    void updateProductTypeFromRequest(UpdateProductTypeRequest request, @MappingTarget ProductType productType);
}
