package net.taviscaron.airliners.parser;

public class ParserFactory {
    public static Parser createParser() {
        return new ParserImpl();
    }
}
