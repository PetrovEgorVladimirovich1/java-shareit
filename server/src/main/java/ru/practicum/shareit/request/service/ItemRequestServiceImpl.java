package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.FailIdException;
import ru.practicum.shareit.item.service.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.dto.ItemRequestWithItemDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository repository;

    private final UserService userService;

    private final ItemRepository itemRepository;

    @Override
    public ItemRequestDto create(Long userId, ItemRequestDto itemRequestDto) {
        userService.getByIdUser(userId);
        ItemRequest itemRequest = ItemRequestMapper.toItemRequest(itemRequestDto);
        itemRequest.setRequestor(userId);
        itemRequest.setCreated(LocalDateTime.now());
        log.info("Запрос успешно добавлен. {}", itemRequest);
        return ItemRequestMapper.toItemRequestDto(repository.save(itemRequest));
    }

    @Override
    public List<ItemRequestWithItemDto> getItemRequests(Long userId) {
        userService.getByIdUser(userId);
        return repository.findByRequestorOrderByCreatedDesc(userId).stream()
                .map(itemRequest -> ItemRequestMapper.toItemRequestWithItemDto(itemRequest,
                        itemRepository.findByRequestId(itemRequest.getId())))
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemRequestWithItemDto> getItemRequestsOtherUsers(Long userId, int from, int size) {
        userService.getByIdUser(userId);
        return repository.findByRequestorNot(userId,
                        PageRequest.of(from / size, size, Sort.by("created").descending()))
                .stream()
                .map(itemRequest -> ItemRequestMapper.toItemRequestWithItemDto(itemRequest,
                        itemRepository.findByRequestId(itemRequest.getId())))
                .collect(Collectors.toList());
    }

    @Override
    public ItemRequestWithItemDto getByIdItemRequest(Long requestId, Long userId) {
        userService.getByIdUser(userId);
        Optional<ItemRequest> itemRequest = repository.findById(requestId);
        if (itemRequest.isEmpty()) {
            throw new FailIdException("Неверный id!");
        }
        if (itemRequest.get().getRequestor().equals(userId)) {
            return ItemRequestMapper.toItemRequestWithItemDto(repository.findByIdAndRequestor(requestId, userId),
                    itemRepository.findByRequestId(requestId));
        }
        return ItemRequestMapper.toItemRequestWithItemDto(itemRequest.get(), itemRepository.findByRequestId(requestId));
    }
}
