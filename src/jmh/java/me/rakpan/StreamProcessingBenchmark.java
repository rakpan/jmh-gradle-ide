package me.rakpan;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.*;
import java.util.stream.IntStream;

/**
 * @author Rakesh Panati
 */
@State(Scope.Thread)
public class StreamProcessingBenchmark {

	@Setup
	public void setup() {
		//ary = new int[4096];
	}

	@Benchmark
	public void testStreamWithoutMap() {
		List<AbstractMap.SimpleEntry<Integer,String>> sourceList = new ArrayList<>();
		List<AbstractMap.SimpleEntry<Integer,String>> targetList = new ArrayList<>();

		IntStream.range(0,100).forEach(entry -> sourceList.add(new AbstractMap.SimpleEntry<>(entry, "Value " + entry)));

		new Random().ints(100,0,100)
				.forEach(i -> targetList.add(new AbstractMap.SimpleEntry(i,null)));

		targetList.forEach(target -> sourceList.stream()
				.filter(source -> source.getKey().equals(target.getKey()))
				.findFirst()
				.ifPresent(source -> target.setValue(source.getValue())));
	}

	@Benchmark
	public void processEntriesWithMap() {
		List<AbstractMap.SimpleEntry<Integer,String>> sourceList = new ArrayList<>();
		List<AbstractMap.SimpleEntry<Integer,String>> targetList = new ArrayList<>();

		IntStream.range(0,100).forEach(entry -> sourceList.add(new AbstractMap.SimpleEntry<>(entry, "Value " + entry)));

		new Random().ints(100,0,100)
				.forEach(i -> targetList.add(new AbstractMap.SimpleEntry(i,null)));

		Map<Integer,String> finalSource = new HashMap<>();

		sourceList
				.forEach(entry -> finalSource.put(entry.getKey(),entry.getValue()));
		targetList.forEach(target -> target.setValue(finalSource.get(target.getKey())));
	}

	public static void main(String[] args) throws Exception {
		Options options = new OptionsBuilder()
				.include(StreamProcessingBenchmark.class.getSimpleName())
				.warmupIterations(5)
				.measurementIterations(10)
				.forks(2)
				.build();
		new Runner(options).run();
	}
}
