package br.com.lacosflores.controllers;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.lacosflores.dao.FloriculturaDao;
import br.com.lacosflores.dao.UsuarioDao;
import br.com.lacosflores.models.Floricultura;
import br.com.lacosflores.models.Usuario;

@RestController
@CrossOrigin
public class UsuarioController {
	// TEM QUE COLOCAR AS CHAVES AQUI

	@Autowired
	private UsuarioDao usuarioDao;
	@Autowired
	private FloriculturaDao floriculturaDao;

	@RequestMapping(value = "/usuario/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Usuario> salvar(@PathVariable("id") Long id, @RequestBody Usuario usuario) {
		try {
			usuarioDao.salvar(usuario);
			//HttpHeaders responseHeader = new HttpHeaders();
			//URI location = new URI("/usuario" + id);
			//responseHeader.setLocation(location);
			//return new ResponseEntity<Void>(responseHeader, HttpStatus.OK);
			return ResponseEntity.ok(usuario);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/{id}/usuario", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Usuario> inserir(@PathVariable("id") Long id, @RequestBody Usuario usuario) {
		Floricultura floricultura = floriculturaDao.consultar(id);
		usuario.setFloricultura(floricultura);
		
		usuarioDao.inserir(usuario);

		try {
			URI location = new URI("/usuario" + usuario.getId());
			return ResponseEntity.created(location).body(usuario);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/usuario/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> remover(@PathVariable("id") long id) {
		usuarioDao.remover(id);
		return ResponseEntity.noContent().build();
	}

	@RequestMapping(value = "/usuario", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public List<Usuario> listar() {
		return usuarioDao.listar();
	}

	@RequestMapping(value = "/usuario/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Usuario consultar(@PathVariable("id") long id) {
		return usuarioDao.consultar(id);
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Usuario> logar(@RequestBody Usuario usuario) {
		Usuario us = usuarioDao.logar(usuario);

		if (us != null) {
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
	}

}
