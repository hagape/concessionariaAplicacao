package concessionaria.excelsior.com.br.concessionariaexcelsior.api

import concessionaria.excelsior.com.br.concessionariaexcelsior.model.Usuario
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface UsuarioAPI {
    @POST("/usuario/autentica")
    fun buscarUsuario(@Body usuario: Usuario): Call<Usuario>

    @POST("usuario")
    fun salvarUsuario(@Body usuario: Usuario): Call<Void>

}