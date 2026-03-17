package com.example.loginframe.Service;

import com.example.loginframe.Entity.Contact;
import com.example.loginframe.Entity.IsoStandard;
import com.example.loginframe.Repository.ContactRepository;
import com.example.loginframe.Repository.IsoStandardRepository;
import com.example.loginframe.dto.ContactDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ContactService {

    private final ContactRepository contactRepository;
    private final IsoStandardRepository isoStandardRepository;

    public ContactDto saveContact(ContactDto contactDTO) {

        List<IsoStandard> isoStandards = new ArrayList<>();

        if (contactDTO.getIsoStandardCodes() != null && !contactDTO.getIsoStandardCodes().isEmpty()) {
            isoStandards = isoStandardRepository.findByIsoCodeIn(contactDTO.getIsoStandardCodes());

            if (isoStandards.size() != contactDTO.getIsoStandardCodes().size()) {
                throw new IllegalArgumentException("One or more ISO codes are invalid");
            }
        }

        Contact contact = Contact.builder()
                .fullName(contactDTO.getFullName())
                .email(contactDTO.getEmail())
                .phone(contactDTO.getPhone())
                .companyName(contactDTO.getCompanyName())
                .message(contactDTO.getMessage())
                .isoStandards(isoStandards)
                .build();

        Contact savedContact = contactRepository.save(contact);

        return mapToDTO(savedContact);
    }

    private ContactDto mapToDTO(Contact contact) {
        return ContactDto.builder()
                .id(contact.getId())
                .fullName(contact.getFullName())
                .email(contact.getEmail())
                .phone(contact.getPhone())
                .companyName(contact.getCompanyName())
                .message(contact.getMessage())
                .isoStandardCodes(
                        contact.getIsoStandards() != null
                                ? contact.getIsoStandards()
                                .stream()
                                .map(IsoStandard::getIsoCode)
                                .collect(Collectors.toList())
                                : new ArrayList<>()
                )
                .build();
    }
}