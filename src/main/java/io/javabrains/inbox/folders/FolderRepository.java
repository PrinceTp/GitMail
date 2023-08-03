package io.javabrains.inbox.folders;

import java.util.List;

import org.springframework.data.cassandra.repository.CassandraRepository;

public interface FolderRepository extends CassandraRepository<Folder, String> {

    List<Folder> findAllById(String id);

}
