import java.util.*;
import java.util.function.Consumer;

/**
 * Created by Tianjie Wen and Joshua Beheln on 4/17/17.
 */


public class MyList<T> implements Iterable<T> {


	private Object[] p;
	private int size = 0;

	private static final Object[] defaultList = {};


	//Without define capacity
	public MyList() {
		this.p = defaultList;
	}

	public int size() {
		return size;
	}

	public boolean isEmpty() {
		return size == 0;
	}

	public boolean contains(Object o) {
		return indexOf(o) >= 0;
	}

	public int indexOf(Object o) {

		for (int i = 0; i < size; i++) {
			if (o.equals(p[i])) {
				return i;
			}
		}
		//not found
		return -1;
	}

	public T get(int index) {
		//index check
		if (index < 0 || index > size - 1) {
			throw new NullPointerException("Index out of bound");
		}
		return (T) p[index];
	}


	public boolean remove(Object o) {
		//remove from an empty list
		if (isEmpty()) {
			throw new NullPointerException("Delete from a empty list");
		}

		for (int i = 0; i < size; i++) {
			if (p[i].equals(o)) {
				for (int j = i; j < size - 1; j++) {
					p[j] = p[j + 1];
				}
				p = Arrays.copyOf(p, size - 1);
				size--;
				return true;
			}
		}
		return false;
	}


	//always add in the back
	public void add(Object o) {
		p = Arrays.copyOf(p, size + 1);
		p[size] = o;
		size++;
	}


	public void sort(Comparator<T> c) {
		if (isEmpty() && size == 1) {
			return;
		}

		//insertion sort
		for (int i = 1; i < size; i++) {
			int j = i;
			while (j > 0 && c.compare((T) p[j], (T) p[j - 1]) < 0) {
				swap(j-1, j);
				--j;
			}
		}

	}

	private void swap(int front, int back) {
		Object temp = p[back];
		for (int i = back; i > front; i--) {
			p[i] = p[i-1];
		}
		p[front] = temp;
	}


	@Override
	public Iterator<T> iterator() {
		class MyIterator<T> implements Iterator<T> {

			private int start = 0, end = size - 1;
			private int cursor = start - 1;


			@Override
			public boolean hasNext() {
				return cursor + 1 < size;
			}

			@Override
			public T next() {
				if (!hasNext()) {
					return null;
				}
				cursor++;
				return (T) p[cursor];
			}
		}
		return new MyIterator<>();
	}

	@Override
	public void forEach(Consumer<? super T> action) {
		Objects.requireNonNull(action);
		final T[] elementData = (T[]) this.p;
		final int size = this.size;
		for (int i = 0; i < size; i++) {
			action.accept(elementData[i]);
		}
	}


	public static void main(String[] args) {
		MyList<String> test = new MyList<>();
		String testString1 = "7";
		String testString2 = "4";
		String testString3 = "8";
		String testString4 = "2";
		String testString5 = "1";
		String testString6 = "3";
		String testString7 = "6";

		test.add(testString1);
		test.add(testString2);
		test.add(testString3);
		test.add(testString4);
		test.add(testString5);
		test.add(testString6);
		test.add(testString7);

		class StringComparator implements Comparator<String> {

			@Override
			public int compare(java.lang.String o1, java.lang.String o2) {
				return o1.compareTo(o2);
			}
		}
		test.sort(new StringComparator());
		for (String s :
				test) {
			System.out.println(s);
		}

	}
}
