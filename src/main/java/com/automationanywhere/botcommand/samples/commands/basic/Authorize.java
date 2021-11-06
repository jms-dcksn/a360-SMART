/*
 * Copyright (c) 2020 Automation Anywhere.
 * All rights reserved.
 *
 * This software is the proprietary information of Automation Anywhere.
 * You shall use it only in accordance with the terms of the license agreement
 * you entered into with Automation Anywhere.
 */

/**
 * @author James Dickson
 */
package com.automationanywhere.botcommand.samples.commands.basic;

import static com.automationanywhere.commandsdk.model.AttributeType.CREDENTIAL;
import static com.automationanywhere.commandsdk.model.AttributeType.TEXT;
import static com.automationanywhere.commandsdk.model.DataType.STRING;

import com.automationanywhere.botcommand.data.impl.StringValue;
import com.automationanywhere.botcommand.exception.BotCommandException;
import com.automationanywhere.botcommand.samples.Utils.HTTPRequest;

import java.io.File;
import java.security.PrivateKey;
import java.time.Instant;
import java.util.UUID;

import com.automationanywhere.botcommand.samples.Utils.PemUtils;
import com.automationanywhere.commandsdk.annotations.BotCommand;
import com.automationanywhere.commandsdk.annotations.CommandPkg;
import com.automationanywhere.commandsdk.annotations.Execute;
import com.automationanywhere.commandsdk.annotations.Idx;
import com.automationanywhere.commandsdk.annotations.Pkg;
import com.automationanywhere.commandsdk.annotations.rules.NotEmpty;
import com.automationanywhere.commandsdk.model.AttributeType;
import com.automationanywhere.core.security.SecureString;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * @author James Dickson
 */
@BotCommand
@CommandPkg(label = "Authorize",
		description = "Retrieves Access Token using SMART Backend Services Authorization",
		icon = "smart.svg",
		name = "smartBackend",
		node_label = "Authorize with Private Key",
		comment = true,
		return_required = true,
		return_label = "Access Token",
		return_type = STRING,
		return_description = "Returns access token in a string variable"
		)
public class Authorize {

	@Execute
	public StringValue execute(
			@Idx(index = "1", type = TEXT) @Pkg(label = "Base URL", description = "e.g. https://{hostname}/{instance}/ " +
					"e.g. for EPIC sandbox https://fhir.epic.com/interconnect-fhir-oauth/") @NotEmpty String url,
			@Idx(index = "2", type = TEXT) @Pkg(label = "Token Endpoint", description = "e.g. oauth2/token for FHIR EPIC sandbox")
			@NotEmpty String tokenEndpoint,
			@Idx(index = "3", type = CREDENTIAL) @Pkg(label = "Client ID") @NotEmpty SecureString clientId,
			@Idx(index = "4", type = AttributeType.FILE) @Pkg(label = "Private Key File (.pem)", description = "Private Key file associated with the RSA public key " +
					"used to register the application with the FHIR server") @NotEmpty String pemFile
	) {
		String ins_clientId = clientId.getInsecureString();
		String response = "";
		String accessToken = "";
		String authURL = url + tokenEndpoint;
		Instant now = Instant.now();
		long epochValue = now.getEpochSecond() + 240;
		int epochInt = (int) epochValue;
		try {
			PrivateKey key = PemUtils.pemFileLoadPrivateKeyPkcs1OrPkcs8Encoded(new File(pemFile));
			String jwt = Jwts.builder()
					.claim("iss", ins_clientId)
					.claim("sub", ins_clientId)
					.claim("aud", authURL)
					.claim("jti", UUID.randomUUID().toString())
					.claim("exp", epochInt)
					.signWith(key, SignatureAlgorithm.RS384)
					.compact();
			response = HTTPRequest.oAuthMethod(authURL, jwt);
			Object obj = new JSONParser().parse(response);
			JSONObject jsonResponse = (JSONObject) obj;
			accessToken = (String) jsonResponse.get("access_token");

		} catch (Exception e) {
			throw new BotCommandException("The response from the server did not contain an access token. Please check your credentials. Exception message: " + response + e);
		}
		return new StringValue(accessToken);
	}
}
