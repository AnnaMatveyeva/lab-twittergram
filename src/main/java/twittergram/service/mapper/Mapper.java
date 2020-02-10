package twittergram.service.mapper;

public interface Mapper<T, V> {

    V toDTO(T entity);
}
