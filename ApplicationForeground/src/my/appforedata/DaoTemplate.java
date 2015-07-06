package my.appforedata;

import java.util.List;


public interface DaoTemplate<T> {

	long save(T type);


	T get(long id);

	List<T> getAll();
//	T buildRecordFromCursor(Cursor C);
	void update(T type);

	void delete(T type);

}
