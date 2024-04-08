package ru.practicum.shareit.request.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

@Repository
public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {
    List<ItemRequest> findByRequestorOrderByCreatedDesc(Long requestor);

    List<ItemRequest> findByRequestorNot(Long requestor, PageRequest page);

    ItemRequest findByIdAndRequestor(Long id, Long requestor);
}
