/*
 * Copyright 2019 Valter Luís Estevam Junior
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 * 
 */
package br.edu.ifpr.irati.mb.util;

import br.edu.ifpr.irati.dao.Dao;
import br.edu.ifpr.irati.dao.GenericDAO;
import br.edu.ifpr.irati.exception.PersistenceException;
import br.edu.ifpr.irati.modelo.Categoria;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 * Conversor de Categoria para String e vice-versa.
 * 
 * @author Valter Estevam
 */
@FacesConverter(forClass = Categoria.class, value = "categoriaConverter")
public class CategoriaConverter implements Converter {

    /**
     *
     * @param context
     * @param component
     * @param value identificador no formato de uma String.
     * @return Objeto da classe Categoria
     */
    @Override
    public Object getAsObject(FacesContext context, UIComponent component,
            String value) {
        if (value == null || value.isEmpty()) {
            return null;
        } else {
            Integer id = Integer.parseInt(value);
            Dao<Categoria> categoriaDAO = new GenericDAO<>(Categoria.class);
            Categoria categoria;
            try {
                categoria = categoriaDAO.buscarPorId(id);                
            } catch (PersistenceException ex) {
                return new Categoria();
            }
            return categoria;
        }
    }

    /**
     *
     * @param context
     * @param component
     * @param value objeto da classe Categoria
     * @return identificador único convertido em String.
     */
    @Override
    public String getAsString(FacesContext context, UIComponent component,
            Object value) {
        Categoria categoria = (Categoria) value;        
        if (categoria != null) {
            return String.valueOf(categoria.getIdCategoria());
        } else {
            return null;
        }
    }

}