package elements.auth.main;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import be.objectify.deadbolt.core.models.Permission;
import com.avaje.ebean.Model;

/**
 * Initial version based on work by Steve Chaloner (steve@objectify.be) for
 * Deadbolt2
 */
@Entity
@Table(name = "user_permissions")
public class EntryUserPermission extends Model implements Permission {

	private static final long serialVersionUID = 1L;

	@Id
	public Long id;

	public String value;

	public static final Model.Finder<Long, EntryUserPermission> find = new Model.Finder<Long, EntryUserPermission>(
			Long.class, EntryUserPermission.class);

	public String getValue() {
		return value;
	}

	public static EntryUserPermission findByValue(String value) {
		return find.where().eq("value", value).findUnique();
	}
}
