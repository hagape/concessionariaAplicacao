package concessionaria.excelsior.com.br.concessionariaexcelsior.api

import concessionaria.excelsior.com.br.concessionariaexcelsior.model.Carro
import concessionaria.excelsior.com.br.concessionariaexcelsior.model.Usuario
import retrofit2.Call
import retrofit2.http.*

interface CarroAPI {

    @GET("/carro")
    fun buscarCarros(): Call<List<Carro>>

    @GET("/carro/{marca}")
    fun buscarPorMarca(@Path("marca") marca: String): Call<List<Carro>>

    @POST("/carro")
    fun salvar(@Body carro: Carro): Call<Void>

    @PUT("/carro")
    fun update(@Body carro: Carro): Call<Void>

    @DELETE("/carro/{id}/deletar")
    fun delete(@Path("id") id: String): Call<Void>
}