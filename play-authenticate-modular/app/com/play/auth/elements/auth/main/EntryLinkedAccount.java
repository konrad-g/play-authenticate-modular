package com.play.auth.elements.auth.main;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.avaje.ebean.Model;
import com.feth.play.module.pa.user.AuthUser;

@Entity
@Table(name = "linked_accounts")
public class EntryLinkedAccount extends Model {

	private static final long serialVersionUID = 1L;

	@Id
	public Long id;

	@ManyToOne
	public EntryUser user;

	public String providerUserId;
	public String providerKey;

	public static final Finder<Long, EntryLinkedAccount> find = new Finder<Long, EntryLinkedAccount>(
			Long.class, EntryLinkedAccount.class);

	public static EntryLinkedAccount findByProviderKey(final EntryUser user, String key) {
		return find.where().eq("user", user).eq("providerKey", key)
				.findUnique();
	}

	public static EntryLinkedAccount create(final AuthUser authUser) {
		final EntryLinkedAccount ret = new EntryLinkedAccount();
		ret.update(authUser);
		return ret;
	}
	
	public void update(final AuthUser authUser) {
		this.providerKey = authUser.getProvider();
		this.providerUserId = authUser.getId();
	}

	public static EntryLinkedAccount create(final EntryLinkedAccount acc) {
		final EntryLinkedAccount ret = new EntryLinkedAccount();
		ret.providerKey = acc.providerKey;
		ret.providerUserId = acc.providerUserId;

		return ret;
	}
}