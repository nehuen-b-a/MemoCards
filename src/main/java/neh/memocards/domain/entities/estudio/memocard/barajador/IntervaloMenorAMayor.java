package neh.memocards.domain.entities.estudio.memocard.barajador;

import lombok.NoArgsConstructor;
import neh.memocards.domain.entities.estudio.memocard.MemoCard;
import utils.CircularList;


import java.util.*;

@NoArgsConstructor
public class IntervaloMenorAMayor implements ITipoDeBarajador {

    @Override
    public CircularList<MemoCard> barajarComienzoDeSesion(List<MemoCard> memoCardsNuevas, List<MemoCard> memoCardsRepaso) {
        CircularList<MemoCard> memoCardsBarajadas = new CircularList<>();
        memoCardsBarajadas.addAll(memoCardsNuevas);
        memoCardsBarajadas.addAll(memoCardsRepaso);

        return barajar(memoCardsBarajadas);
    }

    @Override
    public CircularList<MemoCard> barajar(CircularList<MemoCard> memoCards) {
        //Desordeno antes de Ordenar, para evitar patornes repetidos en el orden de las tarjetas
        List<MemoCard> memoCardsPorBarajadar = memoCards.getElements();
        Collections.shuffle(memoCardsPorBarajadar);

        //Ordeno
        List<MemoCard> repaso = memoCardsPorBarajadar.stream().sorted(Comparator.comparingInt(MemoCard::getIntentos)).toList();

        //Retorno el valor como CircularList
        CircularList<MemoCard> memoCardsBarajadas = new CircularList<>();
        memoCardsBarajadas.addAll(repaso);

        return memoCardsBarajadas;
    }


}
