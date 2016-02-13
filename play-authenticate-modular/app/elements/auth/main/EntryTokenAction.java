package elements.auth.main;

import java.util.Date;

import javax.persistence.*;

import play.data.format.Formats;
import com.avaje.ebean.Model;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.QueryIterator;
import com.avaje.ebean.annotation.EnumValue;

@Entity
@Table(name = "token_actions")
public class EntryTokenAction extends Model {

	public enum Type {
		@EnumValue("EV")
		EMAIL_VERIFICATION,

		@EnumValue("PR")
		PASSWORD_RESET
	}

	private static final long serialVersionUID = 1L;

	/**
	 * Verification time frame (until the user clicks on the link in the email)
	 * in seconds
	 * Defaults to one week
	 */
	private final static long VERIFICATION_TIME = 7 * 24 * 3600;

	@Id
	public Long id;

	@Column(unique = true)
	public String token;

	@ManyToOne
	public EntryUser targetUser;

	public Type type;

	@Formats.DateTime(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date created;

	@Formats.DateTime(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date expires;

	public static final Finder<Long, EntryTokenAction> find = new Finder<Long, EntryTokenAction>(
			Long.class, EntryTokenAction.class);

	public static EntryTokenAction findByToken(final String token, final Type type) {
		return find.where().eq("token", token).eq("type", type).findUnique();
	}

	public static void deleteByUser(final EntryUser u, final Type type) {
		QueryIterator<EntryTokenAction> iterator = find.where()
				.eq("targetUser.id", u.id).eq("type", type).findIterate();
		Ebean.delete(iterator);
		iterator.close();
	}

	public boolean isValid() {
		return this.expires.after(new Date());
	}

	public static EntryTokenAction create(final Type type, final String token,
										  final EntryUser targetUser) {
		final EntryTokenAction ua = new EntryTokenAction();
		ua.targetUser = targetUser;
		ua.token = token;
		ua.type = type;
		final Date created = new Date();
		ua.created = created;
		ua.expires = new Date(created.getTime() + VERIFICATION_TIME * 1000);
		ua.save();
		return ua;
	}
}
