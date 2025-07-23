package ism.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ism.entity.Client;
import ism.model.ClientDTO;
import ism.repository.ClientRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service @RequiredArgsConstructor
public class ClientService {
    private final ClientRepository clientRepo;
    private final ModelMapper mapper;

    @Transactional
    public ClientDTO createClient(ClientDTO dto) {
        Client c = mapper.map(dto, Client.class);
        Client saved = clientRepo.save(c);
        return mapper.map(saved, ClientDTO.class);
    }

    @Transactional
    public List<ClientDTO> getAllClients() {
        return clientRepo.findAll().stream()
                .map(c -> mapper.map(c, ClientDTO.class))
                .collect(Collectors.toList());
    }

    @Transactional
    public ClientDTO getClient(Long id) {
        return clientRepo.findById(id)
                .map(c -> mapper.map(c, ClientDTO.class))
                .orElseThrow(() -> new RuntimeException("Client not found"));
    }
}