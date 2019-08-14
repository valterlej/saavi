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
package br.edu.ifpr.irati.mb.administracao;

import br.edu.ifpr.irati.mb.util.CrudMB;
import br.edu.ifpr.irati.modelo.Categoria;
import java.io.Serializable;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ManagedBean;

/**
 *
 * Controlador da interface de gerenciamento de categorias.
 * 
 * @author Valter Estevam
 */
@ManagedBean
@SessionScoped
public class CategoriaMB extends CrudMB<Categoria> implements Serializable {

    /**
     * Construtor padrão.
     */
    public CategoriaMB() {
        super(Categoria.class,"/administracao/categoria","/administracao/administrador");
    }
    
}