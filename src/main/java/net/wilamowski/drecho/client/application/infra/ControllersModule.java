//package net.wilamowski.drecho.client.application.infra;
//
///*Author:
//Arkadiusz Wilamowski
//https://github.com/szwrk
//*/
//
//import dagger.Module;
//import dagger.Provides;
//import dagger.multibindings.ClassKey;
//import dagger.multibindings.IntoMap;
//import net.wilamowski.drecho.client.presentation.login.LoginController;
//import net.wilamowski.drecho.configuration.backend_ports.AuthenticatorService;
//import net.wilamowski.drecho.standalone.domain.user.UserService;
//import net.wilamowski.drecho.standalone.persistance.impl.inmemory.UserRepositoryInMemory;
//import net.wilamowski.drecho.standalone.service.authenticator.AuthenticatorServiceImpl;
//
//import javax.inject.Provider;
//import javax.inject.Singleton;
//import java.util.HashMap;
//import java.util.Map;
//
//@Module
//public class ControllersModule {
//  @Provides
//  Map<Class<?>, Provider<Object>> provideMyMap() {
//    return new HashMap<>();
//  }
//  @Provides
//  @Singleton
//  UserService provideUserService() {
//    return new UserService(UserRepositoryInMemory.createUserRepositoryInMemory());
//  }
//  @Provides
//  @IntoMap
//  @Singleton
//  @ClassKey(AuthenticatorService.class)
//  AuthenticatorService provideAuthenticatorService() {
//    return new AuthenticatorServiceImpl(
//            new UserService( UserRepositoryInMemory.createUserRepositoryInMemory()));
//  }
//
//  @Provides
//  @IntoMap
//  @ClassKey(LoginController.class)
//  LoginController  provideLoginController(AuthenticatorServiceImpl authenticatorService) {
//    return new LoginController(authenticatorService);
//  }
//}
//
