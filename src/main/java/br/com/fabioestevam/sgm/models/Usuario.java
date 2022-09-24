package br.com.fabioestevam.sgm.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Data;

@Data
@Entity
public class Usuario implements UserDetails, Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@Column(nullable=false)
	private String nome;
	@Column(nullable=false, unique=true)
	private String email;
	@Column(nullable=true, length = 11)
	private String telefone;
	@Column(nullable=false)
	private String senha;
	@Column(nullable=true)
	private String tokenRecuperacaoSenha;
	
	@ManyToMany
	@JoinTable(name= "usuario_regra", joinColumns = @JoinColumn(name = "usuario_id"), inverseJoinColumns = @JoinColumn(name="regra_id"))
	private List<Regra> regras = new ArrayList<>();
	
	@OneToMany(mappedBy = "responsavel", fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	private List<Convite> convites = new ArrayList<>();
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.regras;
	}
	
	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return this.senha;
	}
	
	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return this.email;
	}
	
	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}
	
	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}
	
	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}
	
	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}
	
}
