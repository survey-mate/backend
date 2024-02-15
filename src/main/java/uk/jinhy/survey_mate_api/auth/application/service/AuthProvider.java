package uk.jinhy.survey_mate_api.auth.application.service;

public interface AuthProvider {

    void setAuthentication(String token);

    String getUsernameFromAuthentication();

    String generateToken(String id, String password);
}
