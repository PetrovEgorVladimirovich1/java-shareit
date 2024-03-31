package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestWithItemDto;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDto create(Long userId, ItemRequestDto itemRequestDto);

    List<ItemRequestWithItemDto> getItemRequests(Long userId);

    List<ItemRequestWithItemDto> getItemRequestsOtherUsers(Long userId, int from, int size);

    ItemRequestWithItemDto getByIdItemRequest(Long requestId, Long userId);
}
