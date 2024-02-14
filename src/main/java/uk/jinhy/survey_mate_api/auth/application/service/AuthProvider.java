package uk.jinhy.survey_mate_api.auth.application.service;

public interface AuthProvider {

    String getUsernameFromAuthentication();

    String generateToken(String id, String password);
}
