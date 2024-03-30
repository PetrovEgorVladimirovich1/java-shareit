package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestWithItemDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

/**
 * TODO Sprint add-item-requests.
 */
@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Validated
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestDto create(@Valid @RequestBody ItemRequestDto itemRequestDto,
                                 @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemRequestService.create(userId, itemRequestDto);
    }

    @GetMapping
    public List<ItemRequestWithItemDto> getItemRequests(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemRequestService.getItemRequests(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestWithItemDto> getItemRequestsOtherUsers(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                                  @RequestParam(name = "from", defaultValue = "0")
                                                                  @PositiveOrZero int from,
                                                                  @RequestParam(name = "size", defaultValue = "10")
                                                                  @Positive int size) {
        return itemRequestService.getItemRequestsOtherUsers(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ItemRequestWithItemDto getByIdItemRequest(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                     @PathVariable Long requestId) {
        return itemRequestService.getByIdItemRequest(requestId, userId);
    }
}
