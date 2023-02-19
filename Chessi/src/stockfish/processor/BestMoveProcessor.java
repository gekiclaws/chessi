package stockfish.processor;

import stockfish.exception.UCIRuntimeException;
import stockfish.model.BestMove;
import stockfish.parser.BestMoveParser;

import java.util.List;

public class BestMoveProcessor extends UCICommandProcessor<BestMove> {

    protected static BestMoveParser bestMoveParser = new BestMoveParser();

    @Override
    public BestMove process(List<String> list) {
        return list.stream()
                .filter(bestMoveParser::matches)
                .findFirst()
                .map(bestMoveParser::parse)
                .orElseThrow(()->new UCIRuntimeException("Cannot find best movement in engine output!\n"));
    }
}
