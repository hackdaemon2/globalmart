package com.example.demo.utils;

import com.example.demo.configurations.ApplicationContextProvider;
import com.example.demo.exception.BadRequestException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;

import java.util.Objects;

@Slf4j
public final class Logger {

    private Long benchmark;
    private String message;
    private String uniqueID;
    private Exception exception;

    private static final String REQUEST_MESSAGE = "Incoming Request: ";

    private static Logger builder(String message) {
        Logger logger = new Logger();
        logger.message = message;
        return logger;
    }

    public static long start() {
        return System.currentTimeMillis();
    }

    public static void logIncomingRequest(HttpServletRequest request, Object body, long start) {
        StringBuilder reqMessage = new StringBuilder();

        if (Objects.nonNull(body)) {
            String payload = JsonMapper.toJson(body);
            reqMessage.append("Incoming Request: ").append(payload);
        } else {
            reqMessage.append(REQUEST_MESSAGE).append("null");
        }

        String method = request.getMethod();
        String emptyMessage = reqMessage.toString().replace(REQUEST_MESSAGE, "");
        boolean postRequest = method.equalsIgnoreCase(HttpMethod.POST.name()) && "{}".equals(emptyMessage);

        if (postRequest) {
            throw new BadRequestException("missing request parameters");
        }

        Logger.builder(reqMessage.toString()).start(start).info();
    }

    private Logger start(long start) {
        this.benchmark = start;
        return this;
    }

    private Logger uniqueID(String uniqueID) {
        this.uniqueID = uniqueID;
        return this;
    }

    private String logDetails() {
        if (this.benchmark == null) {
            this.benchmark = System.currentTimeMillis();
        }

        String logDetails = String.format("APPLICATION : %s | FILE : %s | METHOD : %s | LINE : %s | TAT : %d ",
                ApplicationContextProvider.getEnvironment().getProperty("app.name"),
                Thread.currentThread().getStackTrace()[3].getFileName(),
                Thread.currentThread().getStackTrace()[3].getMethodName(),
                Thread.currentThread().getStackTrace()[3].getLineNumber(),
                (System.currentTimeMillis() - this.benchmark));

        if (this.uniqueID != null) {
            logDetails += " | UNIQUEID : " + this.uniqueID;
            this.uniqueID = null;
        }

        if (this.message != null) {
            logDetails += " | MESSAGE : " + this.message;
            this.message = null;
        }

        if (this.exception != null) {
            this.exception = null;
        }

        this.benchmark = null;
        return logDetails;
    }

    public static void info(String message, long start, String countryCode, String uniqueID) {
        Logger.builder(message)
                .start(start)
                .uniqueID(uniqueID)
                .info();
    }

    public static void info(String message, long start) {
        Logger.builder(message)
                .start(start)
                .info();
    }

    public static void error(String message, long start) {
        Logger.builder(message)
                .start(start)
                .error();
    }

    public static void error(String message, long start, String countryCode, String uniqueID, Exception exception) {
        Logger.builder(message)
                .start(start)
                .uniqueID(uniqueID)
                .exception(exception)
                .error();
    }

    public static void error(String message, long start, String countryCode, String uniqueID) {
        Logger.builder(message)
                .start(start)
                .uniqueID(uniqueID)
                .error();
    }

    public static void error(String message, long start, Exception exception) {
        Logger.builder(message)
                .start(start)
                .exception(exception)
                .error();
    }

    private void info() {
        log.info(this.logDetails());
    }

    private void error() {
        log.error(this.logDetails());
    }

    private Logger exception(Exception exception) {
        this.exception = exception;
        this.message += "\n";
        this.message += exception.getMessage();
        return this;
    }
}
