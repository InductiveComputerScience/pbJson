package JSON.LinkedListElements.LinkedListElementsFunctions;

import JSON.LinkedListElements.Structures.LinkedListNodeElements;
import JSON.LinkedListElements.Structures.LinkedListElements;
import JSON.structures.Element;

public class LinkedListElementsFunctions {
	public static LinkedListElements CreateLinkedListElements(){
		LinkedListElements ll;

		ll = new LinkedListElements();
		ll.first = new LinkedListNodeElements();
		ll.last = ll.first;
		ll.last.end = true;

		return ll;
	}

	public static void LinkedListAddElement(LinkedListElements ll, Element value){
		ll.last.end = false;
		ll.last.value = value;
		ll.last.next = new LinkedListNodeElements();
		ll.last.next.end = true;
		ll.last = ll.last.next;
	}

	public static Element[] LinkedListElementsToArray(LinkedListElements ll){
		Element [] array;
		double length, i;
		LinkedListNodeElements node;

		node = ll.first;

		length = LinkedListElementsLength(ll);

		array = new Element [(int)(length)];

		for(i = 0d; i < length; i = i + 1d){
			array[(int)(i)] = node.value;
			node = node.next;
		}

		return array;
	}

	public static double LinkedListElementsLength(LinkedListElements ll){
		double l;
		LinkedListNodeElements node;

		l = 0d;
		node = ll.first;
		for(; !node.end; ){
			node = node.next;
			l = l + 1d;
		}

		return l;
	}

	public static void FreeLinkedListElements(LinkedListElements ll){
		LinkedListNodeElements node, prev;

		node = ll.first;

		for(; !node.end; ){
			prev = node;
			node = node.next;
			delete(prev);
		}

		delete(node);
	}

  public static void delete(Object object){
    // Java has garbage collection.
  }
}
