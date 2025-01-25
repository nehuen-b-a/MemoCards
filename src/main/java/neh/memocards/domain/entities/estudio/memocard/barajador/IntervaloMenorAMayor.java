package neh.memocards.domain.entities.estudio.memocard.barajador;

import lombok.NoArgsConstructor;
import neh.memocards.domain.entities.estudio.memocard.MemoCard;
import utils.CircularList;


import java.util.*;

@NoArgsConstructor
public class IntervaloMenorAMayor implements ITipoBarajador {

    @Override
    public List<MemoCard> barajarComienzoDeSesion(List<MemoCard> memoCardsNuevas, List<MemoCard> memoCardsRepaso, List<MemoCard> memoCardsActivas ) {
        List<MemoCard> memoCardsBarajadas = new ArrayList<>();
        memoCardsBarajadas.addAll(memoCardsNuevas);
        memoCardsBarajadas.addAll(memoCardsRepaso);

        //Barajo
        memoCardsBarajadas = barajar(memoCardsBarajadas);
        memoCardsActivas = barajar(memoCardsActivas);

        //añado Todas las memoCards ya barajadas a la listaActiva
        var barajaMemoCard = new ArrayList<MemoCard>();
        barajaMemoCard.addAll(memoCardsActivas);
        barajaMemoCard.addAll(memoCardsBarajadas);

        return barajaMemoCard;
    }

    @Override
    public List<MemoCard> barajar(List<MemoCard> memoCards) {
        //Desordeno antes de Ordenar, esto evita patrones repetidos en el orden de las tarjetas
        Collections.shuffle(memoCards);

        return ordenarMemoCards(memoCards);
    }

    @Override
    public List<MemoCard> ordenarMemoCards(List<MemoCard> memoCards) {
        return memoCards.stream().sorted(Comparator.comparingInt(MemoCard::getIntentos)).toList();
    }


}
