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
package br.edu.ifpr.irati.dao;

import br.edu.ifpr.irati.exception.PersistenceException;
import java.io.Serializable;
import java.util.List;

/**
 * Interface genérica para definir a assinatura dos métodos básicos de acesso aos dados.
 * 
 * @author Valter Estevam
 * @param <T> tipo de dado para o qual o objeto será criado.
 */
public interface Dao<T> {

    /**
     * Retorna uma entidade a partir do identificador único cadastrado no sistema.
     * 
     * @param id identificador único.
     * @return Objeto da entidade definida na instância da classe concreta.
     * @throws PersistenceException 
     */
    public T buscarPorId(Serializable id) throws PersistenceException;

    /**
     * Cadastra uma nova entidade no banco de dados.
     * 
     * @param t entidade a ser salva no banco de dados.
     * @throws PersistenceException 
     */
    public void salvar(T t) throws PersistenceException;

    /**
     * Altera o registro de uma entidade já existente no banco de dados.
     * 
     * @param t entidade a ser alterada.
     * @throws PersistenceException 
     */
    public void alterar(T t) throws PersistenceException;
    
    /**
     * Exclui uma entidade do banco de dados.
     * 
     * @param t entidade a ser excluída.
     * @throws PersistenceException 
     */
    public void excluir(T t) throws PersistenceException;
    
    /**
     * Busca todas as entidades armazenadas no banco de dados para a classe informada por parâmetro.
     * 
     * @param clazz Classe para a qual se deseja buscar todos os registros.
     * @return lista com todas as entidades.
     * @throws PersistenceException 
     */
    public List<T> buscarTodos(Class<T> clazz) throws PersistenceException;
        
}