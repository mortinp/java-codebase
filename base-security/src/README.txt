para definir un nuevo modelo de autenticacion en JAAS
se nesecitan sobreescribir las siguientes clases:
javax.security.auth.login.Configuration -> se define la clase del el LoginModule
javax.security.auth.spi.LoginModule -> la clase que se ocupa de loguear los usuarios

y en autorizacion:
java.security.Policy -> la provedora de los permisos