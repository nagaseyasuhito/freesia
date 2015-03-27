package com.github.nagaseyasuhito.freesia.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(indexes = { @Index(columnList = "invitationCode") })
@Data
@EqualsAndHashCode(of = { "id" })
@XmlAccessorType(XmlAccessType.FIELD)
public class User implements Serializable {
	private static final long serialVersionUID = 2182736437791701672L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Version
	@XmlTransient
	private Integer version;

	@Column(nullable = false, unique = true)
	private String mailAddress;

	@Column(nullable = false, unique = true)
	private String invitationCode;

	@Column(nullable = false)
	private Integer numberOfInvitees;
}
