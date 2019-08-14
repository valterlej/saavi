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

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 *
 * Classe utilitária para formatar datas e retornar em strings e ser 
 * chamada pela interface gráfica.
 * 
 * @author Valter Estevam
 */
@RequestScoped
@ManagedBean
public class FormatadorData implements Serializable {
    
    
    /**
     * Formatar a data como dia/mês/ano em padrão dd/MM/yyyy.
     * @param data data a ser formata.
     * @return caminho para o qual o sistema deve ser redirecionado.
     */
    public String getData(Date data) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(data);
    }
    
    /**
     * Formatar a data e hora como dia/mês/ano em padrão dd/MM/yyyy.
     * @param dataHora data a ser formatada.
     * @return caminho para o qual o sistema deve ser redirecionado.
     */
    public String getDataHora(Date dataHora) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        return sdf.format(dataHora);
    }
       
}