package neh.memocards.domain.entities.estudio.memocard.barajador;

import lombok.NoArgsConstructor;
import neh.memocards.domain.entities.estudio.memocard.MemoCard;
import utils.CircularList;


import java.util.*;

@NoArgsConstructor
public class IntervaloMenorAMayor implements ITipoBarajador {

    @Override
    public CircularList<MemoCard> barajarComienzoDeSesion(List<MemoCard> memoCardsNuevas, List<MemoCard> memoCardsRepaso, CircularList<MemoCard> memoCardsActivas ) {
        CircularList<MemoCard> memoCardsBarajadas = new CircularList<>();
        memoCardsBarajadas.addAll(memoCardsNuevas);
        memoCardsBarajadas.addAll(memoCardsRepaso);

        //Barajo
        memoCardsBarajadas = barajar(memoCardsBarajadas);
        memoCardsActivas = barajar(memoCardsActivas);

        //añado Todas las memoCards ya barajadas a la listaActiva
        memoCardsActivas.addAll(memoCardsBarajadas.getElements());

        return memoCardsActivas;
    }

    @Override
    public CircularList<MemoCard> barajar(CircularList<MemoCard> memoCards) {
        //Desordeno antes de Ordenar, esto evita patrones repetidos en el orden de las tarjetas
        List<MemoCard> memoCardsPorBarajadar = memoCards.getElements();
        Collections.shuffle(memoCardsPorBarajadar);

        //Ordeno
        List<MemoCard> repaso = ordenarMemoCards(memoCardsPorBarajadar);

        //Retorno el valor como CircularList
        CircularList<MemoCard> memoCardsBarajadas = new CircularList<>();
        memoCardsBarajadas.addAll(repaso);

        return memoCardsBarajadas;
    }

    @Override
    public List<MemoCard> ordenarMemoCards(List<MemoCard> memoCards) {
        return memoCards.stream().sorted(Comparator.comparingInt(MemoCard::getIntentos)).toList();
    }


}
