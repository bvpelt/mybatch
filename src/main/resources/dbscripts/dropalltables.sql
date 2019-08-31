-- spring batch
drop table if exists batch_job_execution,batch_job_execution_context,batch_job_execution_params,batch_job_instance,batch_step_execution,batch_step_execution_context cascade;
drop sequence if exists batch_job_execution_seq,batch_job_seq,batch_step_execution_seq;

drop table if exists beschikkingsbevoegdheid;
drop table if exists databasechangelog,databasechangeloglock;
