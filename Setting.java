import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.bukkit.ChatColor;

import lombok.AllArgsConstructor;

public interface Setting<E extends Enum<E> & Setting<E>>{
	
	Object getData();
	
	default public <T> T as(Class<T> tClass) {
        if (tClass.isPrimitive()) {
            throw new IllegalArgumentException(tClass.getSimpleName() + " is of a primitive type. Disallowed type.");
        }
        //Yeah, we /could/ use the is method below, but for
        //the sake of micro-optimization, we're not.
        if (!tClass.isInstance(getData())) {
            throw new IllegalArgumentException(this + " is not of type " + tClass.getSimpleName() + "\n Has data of " + String.valueOf(getData()));
        }
        return tClass.cast(getData());
	}
	
	default public String asString(){
		return this.as(String.class);
	}
	
	default public int asInt(){
		return this.as(Integer.class);
	}
	
	default public double asDouble(){
		return this.as(Double.class);
	}
	
	default public <T> boolean is(Class<T> tClass) {
		if (tClass.isPrimitive()) {
            throw new IllegalArgumentException(tClass.getSimpleName() + " is of a primitive type. Disallowed type.");
        }
        return tClass.isInstance(getData());
	}
	
	//Might return to try to figure this out later at some point
	/*default public E enumSubclass(){
		if(!this.getClass().isEnum()){
			throw new IllegalArgumentException(this.getClass().getSimpleName() + " is not an enum!");
		}
		return this.getClass();
	}*/
	
	default public Map<String, E> enumValues(){
		Map<String, E> enummap = new HashMap<>();
		for(Enum<E> value : (Enum<E>[]) this.getClass().getEnumConstants()){
			enummap.put(value.name(), (E) value);
		}
		return enummap;
	}
	
	default public <T> List<E> allThatAre(Class<T> tClass){
		return this.enumValues()
				.values()
				.stream()
				.filter(value -> value.is(tClass))
				.collect(Collectors.toList());
		//This ^^ is equal to this:
		/*List<E> values = new ArrayList<E>();
		for(E value : this.enumValues().values()){
			if(value.is(tClass))
				values.add(value);
		}
		return values;*/
	}
