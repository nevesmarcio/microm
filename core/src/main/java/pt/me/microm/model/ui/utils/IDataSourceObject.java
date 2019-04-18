package pt.me.microm.model.ui.utils;

// este interface deve ser utilizado pelos metodos flashMessage para fazer o fetch das propriedades e ir actualizando à responsabilidade do UI
public interface IDataSourceObject<OBJECT_TYPE> {
    public void set(OBJECT_TYPE obj);

    public OBJECT_TYPE get();
}		