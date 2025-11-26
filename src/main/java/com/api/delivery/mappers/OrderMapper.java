package com.api.delivery.mappers;

import com.api.delivery.domain.Order;
import com.api.delivery.dtos.requests.CreateOrderRequest;
import com.api.delivery.dtos.requests.UpdateOrderStatusRequest;
import com.api.delivery.dtos.responses.OrderResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    Order toOrderEntity(CreateOrderRequest request);

    OrderResponse toOrderResponse(Order order);

    List<OrderResponse> toOrderResponseList(List<Order> orders);

    @Mapping(target = "id", ignore = true)
    void updateOrderStatusRequest(UpdateOrderStatusRequest request, @MappingTarget Order order);
}
