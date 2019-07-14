package com.tasks.validation.sanitization.domain;

import java.util.List;
import java.util.UUID;


import lombok.Data;


@Data
public class Carington {

	private UUID relativeId;

	private String externalId = null;

	private List<Carington> items = null;

	private String streetAddress = null;

	private Person personName;

	private Address postalAddress;
}