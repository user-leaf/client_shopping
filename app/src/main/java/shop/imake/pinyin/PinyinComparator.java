package shop.imake.pinyin;

import shop.imake.model.ContactModel;
import shop.imake.widget.SideBar;

import java.util.Comparator;

/**
 * 比较器
 * @author xiaanming
 */
public class PinyinComparator implements Comparator<ContactModel> {

	public int compare(ContactModel o1, ContactModel o2) {
		if (o1.getSortLetters().equals(SideBar.markVip)
				|| o2.getSortLetters().equals("#")) {
			return -1;
		} else if (o1.getSortLetters().equals("#")
				|| o2.getSortLetters().equals(SideBar.markVip)) {
			return 1;
		} else {
			return o1.getSortLetters().compareTo(o2.getSortLetters());
		}
	}

}
