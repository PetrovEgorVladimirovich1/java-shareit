package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestClientDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Validated
public class ItemRequestController {
    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity<Object> create(@Valid @RequestBody ItemRequestClientDto itemRequestDto,
                                         @Positive @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemRequestClient.create(userId, itemRequestDto);
    }

    @GetMapping
    public ResponseEntity<Object> getItemRequests(@Positive @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemRequestClient.getItemRequests(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getItemRequestsOtherUsers(@Positive @RequestHeader("X-Sharer-User-Id") Long userId,
                                                            @RequestParam(name = "from", defaultValue = "0")
                                                            @PositiveOrZero int from,
                                                            @RequestParam(name = "size", defaultValue = "10")
                                                            @Positive int size) {
        return itemRequestClient.getItemRequestsOtherUsers(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getByIdItemRequest(@Positive @RequestHeader("X-Sharer-User-Id") Long userId,
                                                     @Positive @PathVariable Long requestId) {
        return itemRequestClient.getByIdItemRequest(requestId, userId);
    }
}
