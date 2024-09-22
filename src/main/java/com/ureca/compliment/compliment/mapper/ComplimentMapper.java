package com.ureca.compliment.compliment.mapper;


import com.ureca.compliment.compliment.Compliment;
import com.ureca.compliment.compliment.dto.ComplimentDTO;
import com.ureca.compliment.user.dto.UserDTO;

public class ComplimentMapper {
     public static ComplimentDTO toDTO(Compliment compliment, UserDTO sender, UserDTO receiver) {
         ComplimentDTO complimentDTO = new ComplimentDTO(
                 compliment.getId(),
                 compliment.getContent(),
                 compliment.isAnonymous(),
                 compliment.getCreatedAt()
         );
         if (sender == null || receiver == null) {
             complimentDTO.setSenderId(compliment.getSenderId());
             complimentDTO.setReceiverId(compliment.getReceiverId());
         } else {
             complimentDTO.setSender(sender);
             complimentDTO.setReceiver(receiver);
         }
         return complimentDTO;
    }
}
