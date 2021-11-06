package com.automationanywhere.botcommand.samples.commands.basic;

import com.automationanywhere.bot.service.GlobalSessionContext;
import com.automationanywhere.botcommand.BotCommand;
import com.automationanywhere.botcommand.data.Value;
import com.automationanywhere.botcommand.exception.BotCommandException;
import com.automationanywhere.commandsdk.i18n.Messages;
import com.automationanywhere.commandsdk.i18n.MessagesFactory;
import com.automationanywhere.core.security.SecureString;
import java.lang.ClassCastException;
import java.lang.Deprecated;
import java.lang.Object;
import java.lang.String;
import java.lang.Throwable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class AuthorizeCommand implements BotCommand {
  private static final Logger logger = LogManager.getLogger(AuthorizeCommand.class);

  private static final Messages MESSAGES_GENERIC = MessagesFactory.getMessages("com.automationanywhere.commandsdk.generic.messages");

  @Deprecated
  public Optional<Value> execute(Map<String, Value> parameters, Map<String, Object> sessionMap) {
    return execute(null, parameters, sessionMap);
  }

  public Optional<Value> execute(GlobalSessionContext globalSessionContext,
      Map<String, Value> parameters, Map<String, Object> sessionMap) {
    logger.traceEntry(() -> parameters != null ? parameters.entrySet().stream().filter(en -> !Arrays.asList( new String[] {}).contains(en.getKey()) && en.getValue() != null).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)).toString() : null, ()-> sessionMap != null ?sessionMap.toString() : null);
    Authorize command = new Authorize();
    HashMap<String, Object> convertedParameters = new HashMap<String, Object>();
    if(parameters.containsKey("url") && parameters.get("url") != null && parameters.get("url").get() != null) {
      convertedParameters.put("url", parameters.get("url").get());
      if(convertedParameters.get("url") !=null && !(convertedParameters.get("url") instanceof String)) {
        throw new BotCommandException(MESSAGES_GENERIC.getString("generic.UnexpectedTypeReceived","url", "String", parameters.get("url").get().getClass().getSimpleName()));
      }
    }
    if(convertedParameters.get("url") == null) {
      throw new BotCommandException(MESSAGES_GENERIC.getString("generic.validation.notEmpty","url"));
    }

    if(parameters.containsKey("tokenEndpoint") && parameters.get("tokenEndpoint") != null && parameters.get("tokenEndpoint").get() != null) {
      convertedParameters.put("tokenEndpoint", parameters.get("tokenEndpoint").get());
      if(convertedParameters.get("tokenEndpoint") !=null && !(convertedParameters.get("tokenEndpoint") instanceof String)) {
        throw new BotCommandException(MESSAGES_GENERIC.getString("generic.UnexpectedTypeReceived","tokenEndpoint", "String", parameters.get("tokenEndpoint").get().getClass().getSimpleName()));
      }
    }
    if(convertedParameters.get("tokenEndpoint") == null) {
      throw new BotCommandException(MESSAGES_GENERIC.getString("generic.validation.notEmpty","tokenEndpoint"));
    }

    if(parameters.containsKey("clientId") && parameters.get("clientId") != null && parameters.get("clientId").get() != null) {
      convertedParameters.put("clientId", parameters.get("clientId").get());
      if(convertedParameters.get("clientId") !=null && !(convertedParameters.get("clientId") instanceof SecureString)) {
        throw new BotCommandException(MESSAGES_GENERIC.getString("generic.UnexpectedTypeReceived","clientId", "SecureString", parameters.get("clientId").get().getClass().getSimpleName()));
      }
    }
    if(convertedParameters.get("clientId") == null) {
      throw new BotCommandException(MESSAGES_GENERIC.getString("generic.validation.notEmpty","clientId"));
    }

    if(parameters.containsKey("pemFile") && parameters.get("pemFile") != null && parameters.get("pemFile").get() != null) {
      convertedParameters.put("pemFile", parameters.get("pemFile").get());
      if(convertedParameters.get("pemFile") !=null && !(convertedParameters.get("pemFile") instanceof String)) {
        throw new BotCommandException(MESSAGES_GENERIC.getString("generic.UnexpectedTypeReceived","pemFile", "String", parameters.get("pemFile").get().getClass().getSimpleName()));
      }
    }
    if(convertedParameters.get("pemFile") == null) {
      throw new BotCommandException(MESSAGES_GENERIC.getString("generic.validation.notEmpty","pemFile"));
    }

    try {
      Optional<Value> result =  Optional.ofNullable(command.execute((String)convertedParameters.get("url"),(String)convertedParameters.get("tokenEndpoint"),(SecureString)convertedParameters.get("clientId"),(String)convertedParameters.get("pemFile")));
      return logger.traceExit(result);
    }
    catch (ClassCastException e) {
      throw new BotCommandException(MESSAGES_GENERIC.getString("generic.IllegalParameters","execute"));
    }
    catch (BotCommandException e) {
      logger.fatal(e.getMessage(),e);
      throw e;
    }
    catch (Throwable e) {
      logger.fatal(e.getMessage(),e);
      throw new BotCommandException(MESSAGES_GENERIC.getString("generic.NotBotCommandException",e.getMessage()),e);
    }
  }
}
