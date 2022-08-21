package com.usa.modelo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Anderson Londoño
 */
@Repository
public interface RepositorioProducto extends CrudRepository<Producto, Integer>{
    
}
