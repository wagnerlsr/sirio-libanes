package br.com.sirio.services;

import br.com.sirio.dtos.UpdateUserDto;
import br.com.sirio.models.User;
import com.fasterxml.jackson.databind.JsonMappingException;

import java.util.List;

public interface UserService {

	List<User> getUsers();
	User getUser(String cpf);
	User updateUser(String cpf, UpdateUserDto userDto, Long userId) throws JsonMappingException;
	User removeUser(String cpf, Long userId);

}
