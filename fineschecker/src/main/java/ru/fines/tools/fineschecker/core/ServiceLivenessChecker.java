package ru.fines.tools.fineschecker.core;

public interface ServiceLivenessChecker {

    String getServiceName();

    boolean isServiceAlive();

}
