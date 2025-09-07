import axios from "axios"
import { AlertTriangle, Check, Minus, Plus, Search, ShoppingCart } from "lucide-react"
import { useEffect, useMemo, useState } from "react"
import { Alert, AlertDescription, AlertTitle } from "~/components/ui/alert"
import { Badge } from "~/components/ui/badge"
import { Button } from "~/components/ui/button"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "~/components/ui/card"
import { Dialog, DialogContent, DialogHeader, DialogTitle } from "~/components/ui/dialog"
import { Input } from "~/components/ui/input"
import { Sheet, SheetContent, SheetHeader, SheetTitle, SheetTrigger } from "~/components/ui/sheet"

export function EcommerceApp() {
    const portBackend = import.meta.env.PORT_BACKEND || 8080;
    const baseURL = `http://localhost:${portBackend}/api/v1`;


    interface Product {
        id: number
        name: string
        price: number
        stock: number
    }
    interface CarrinhoItem extends Product {
        quantity: number,
        unitPrice: number,
        lineTotal: number
    }
    type OrderStatus = 'sucesso' | 'erro' | null;


    const [isCarrinhoAberto, setIsCarrinhoAberto] = useState<boolean>(false)
    const [carrinho, setCarrinho] = useState<CarrinhoItem[]>([])
    const subtotal = carrinho.reduce((total, item) => total + (item.price * item.quantity), 0);
    const [produtos, setProdutos] = useState<Product[]>([])

    const [orderStatus, setOrderStatus] = useState<OrderStatus>(null);
    const [orderResponseErro, setOrderResponseErro] = useState<string>('');

    const [loading, setLoading] = useState(false)

    const [totalPaginas, setTotalPaginas] =  useState(0)
    const [itensPorPagina, setItensPorPagina] = useState(5);
    const [paginaAtual, setPaginaAtual] = useState(1)

    const useDebounce = (value:string, delay:number) => {
        const [debouncedValue, setDebouncedValue] = useState(value)

        useEffect(() => {
            const handler = setTimeout(() => {
                setDebouncedValue(value)
            }, delay)

            return () => {
                clearTimeout(handler)
            }
        }, [value, delay])

        return debouncedValue
    }
    const buscarProdutos = async(page: number, size:number, search: string) => {
        setLoading(true)
        try {
            const response = await axios.get(`${baseURL}/products`, {
                params: {
                    page: page -1,
                    size: itensPorPagina,
                    search: search || undefined
                }
            })

            setProdutos(response.data.content || response.data)
            setTotalPaginas(response.data.totalPages)
        } catch(error) {
            console.error('Erro ao buscar produtos:', error)
        } finally {
            setLoading(false)
        }
    }
    const [buscarPorTermo, setBuscarPorTermo] = useState<string>('')
    const debouncedBuscarPorTermo = useDebounce(buscarPorTermo, 300)
    useEffect(() => {buscarProdutos(paginaAtual, itensPorPagina, debouncedBuscarPorTermo)},
        [paginaAtual, itensPorPagina, debouncedBuscarPorTermo])

    const addItemCarrinho = (product:any) => {
        setCarrinho(prevCarrinho => {
            const itemDuplicado = prevCarrinho.find(item => item.id === product.id)
            if(itemDuplicado) {
                if(itemDuplicado.quantity < product.stock) {
                    return prevCarrinho.map(item => 
                        item.id === product.id ? { ...item, quantity: item.quantity + 1} : item
                    )
                }
                return prevCarrinho
            } else {
                return [...prevCarrinho, {...product, quantity: 1}]
            }
        })    
    }
    const removerDoCarrinho = (id: number) => {
        setCarrinho((prevCarrinho : CarrinhoItem[]) => 
            prevCarrinho.map(item => 
                item.id === id ?
                    { ...item, quantity: item.quantity - 1} :
                    item
            ).filter(item => item.quantity > 0)
        )
    }
    const limparCarrinho = () => {
        setCarrinho([])
    }
    const finalizarCheckout = async () => {
        setLoading(true);
        try {
            const orderItems =  {
                items: carrinho.map(item => ({
                        productId: item.id,
                        quantity: item.quantity
                }))
            }
            const response = await axios.post(`${baseURL}/orders`, orderItems);
            setOrderStatus('sucesso');
            limparCarrinho();
            setIsCarrinhoAberto(false);
            buscarProdutos(1, itensPorPagina, '')
            
        } catch (error: any) {
            // Erro 409 - itens indisponíveis
            if (error.response?.status === 409) {
                setOrderStatus('erro');
                setOrderResponseErro(error.response?.data)
            } else {
                setOrderStatus('erro');
                setOrderResponseErro('Ocorreu um erro ao tentar finalizar o pedido, por favor verificar com o suporte!')
            }
        } finally {
            setLoading(false);
        }
    }
    const fecharAlertaStatusPedido = () => {
        setOrderStatus(null)
    }

    return (
        <div>
            <header  className="border-b">
                <div className="max-w-7xl mx-auto px-4 py-4 flex items-center justify-between">
                    <h1 className="text-3xl font-bold">Sankhya E-Commerce</h1>
                    <Sheet open={isCarrinhoAberto} onOpenChange= {setIsCarrinhoAberto}>
                        <SheetTrigger asChild>
                            <Button
                                variant={"default"}
                                size="icon"
                                className="relative"
                                aria-label={`Abrir carrinho com ${carrinho.length} itens`}
                            >
                                <ShoppingCart className="h-4 w-4"/>
                                {carrinho.length > 0 && (
                                    <Badge  className="absolute -top-2 -right-2 h-6 w-6 rounded-full p-0 flex items-center justify-center">
                                        {carrinho.reduce((total, item) => total + item.quantity, 0)}
                                    </Badge>
                                )}
                            </Button>
                        </SheetTrigger>
                        <SheetContent>
                            <SheetHeader>
                                <SheetTitle>Carrinho de compras</SheetTitle>
                            </SheetHeader>

                            <div className="mt-6 space-y-4">
                                {carrinho.length === 0 ? (
                                <p className="text-muted-foreground text-center py-8">Seu carrinho está vazio</p>
                                ) : (
                                <>
                                    {carrinho.map(item => (
                                    <Card key={item.id}>
                                        <CardContent className="p-4">
                                        <div className="flex justify-between items-start mb-2">
                                            <h4 className="font-medium text-sm">{item.name}</h4>
                                        </div>
                                        <div className="flex items-center justify-between">
                                            <p className="text-lg font-bold text-green-600">
                                            R$ {item.price.toFixed(2).replace('.', ',')}
                                            </p>
                                            <div className="flex items-center gap-2">
                                            <Button
                                                variant="outline"
                                                size="icon"
                                                className="h-8 w-8"
                                                onClick={() => removerDoCarrinho(item.id)}
                                                aria-label={`Diminuir quantidade de ${item.name}`}
                                            >
                                                <Minus className="h-3 w-3" />
                                            </Button>
                                            <span className="w-8 text-center text-sm">{item.quantity}</span>
                                            <Button
                                                variant="outline"
                                                size="icon"
                                                className="h-8 w-8"
                                                onClick={() => addItemCarrinho(item)}
                                                aria-label={`Aumentar quantidade de ${item.name}`}
                                            >
                                                <Plus className="h-3 w-3" />
                                            </Button>
                                            </div>
                                        </div>
                                        </CardContent>
                                    </Card>
                                    ))}
                                    
                                    <div className="border-t pt-4 mt-6">
                                    <div className="flex justify-between items-center mb-4">
                                        <span className="text-lg font-semibold">Subtotal:</span>
                                        <span className="text-xl font-bold text-green-600">
                                        R$ {subtotal.toFixed(2).replace('.', ',')}
                                        </span>
                                    </div>
                                    <Button
                                        onClick={finalizarCheckout}
                                        className="w-full"
                                        size="lg"
                                        aria-label="Finalizar pedido"
                                    >
                                        Finalizar Pedido
                                    </Button>
                                    </div>
                                </>
                                )}
                            </div>
                        </SheetContent>
                    </Sheet>
                </div>
            </header>

            <main className="max-w-7xl mx-auto px-4 py-8">
                <div className="mb-8">
                    <div className="relative max-w-md">
                        <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-muted-foreground h-4 w-4" />
                        <Input
                            type="text"
                            placeholder="Buscar produtos"
                            value={buscarPorTermo}
                            onChange={(e) => setBuscarPorTermo(e.target.value)}
                            className="pl-10"
                            aria-label="Campo de busca de produtos"
                        />
                    </div>

                    {loading && (
                        <div className="text-center py-8">
                            <p className="text-muted-foreground">Carregando produtos...</p>
                        </div>
                    )}
                    { !loading && (
                        <div>
                            {produtos.length === 0 ? (
                                <div>
                                    <p>Nenhum produto encontrado</p>
                                </div>
                            ) : (
                                produtos.map(product => (
                                    <Card key={product.id}>
                                        <CardHeader className="text-lg">
                                            <CardTitle>{product.name}</CardTitle>
                                        </CardHeader>
                                        <CardContent>
                                            <div>
                                                <p>
                                                    R$ {product.price.toFixed(2).replace('.', ',')}
                                                </p>
                                                <div className="flex items-center justify-between">
                                                    <Badge>
                                                        {product.stock > 0 ? `${product.stock} em estoque` : 'Fora de estoque'}
                                                    </Badge>
                                                    <Button
                                                        onClick={() => addItemCarrinho(product)}
                                                        disabled={product.stock === 0}
                                                        aria-label={`Adicionar ${product.name} ao carrinho`}
                                                        >
                                                        Adicionar
                                                    </Button>
                                                </div>
                                            </div>
                                        </CardContent>
                                    </Card>
                                ))
                            )}
                            
                        </div>
                    )}
                    {!loading && produtos.length > 0 && (
                        <div className="flex flex-col sm:flex-row items-center justify-between gap-4">
                            <div className="flex items-center gap-2">
                                <span className="text-sm text-muted-foreground">Itens por página:</span>
                                <select
                                    value={itensPorPagina}
                                    onChange={(e) => setItensPorPagina(Number(e.target.value))}
                                    className="px-3 py-1 border border-input rounded-md text-sm bg-background"
                                    aria-label="Selecionar quantidade de itens por página"
                                >
                                    <option value={2}>2</option>
                                    <option value={5}>5</option>
                                    <option value={10}>10</option>
                                    <option value={20}>20</option>
                                    <option value={50}>50</option>
                                </select>
                            </div>
                            <div className="flex items-center gap-4">
                                <Button
                                    variant="outline"
                                    onClick={() => setPaginaAtual(prev => Math.max(prev - 1, 1))}
                                    disabled={paginaAtual === 1}
                                    aria-label="Página anterior"
                                >
                                    Anterior
                                </Button>

                                <span className="text-sm text-muted-foreground">
                                    Página {paginaAtual} de {totalPaginas}
                                </span>

                                <Button
                                    variant="outline"
                                    onClick={() => setPaginaAtual(prev => Math.min(prev + 1, totalPaginas))}
                                    disabled={paginaAtual === totalPaginas}
                                    aria-label="Próxima página"
                                >
                                    Próxima
                                </Button>
                            </div>
                        </div>
                    )}
                </div>
            </main>

            <Dialog open={!!orderStatus} onOpenChange={() => fecharAlertaStatusPedido()}>
                <DialogContent>
                    <DialogHeader>
                        <DialogTitle>
                            {orderStatus === 'sucesso' ? 'Pedido Realizado!' : 'Erro no Pedido'}
                        </DialogTitle>
                    </DialogHeader>
                
                    <div className="py-4">
                    {orderStatus === 'sucesso' ? (
                        <Alert>
                            <Check className="h-4 w-4" />
                            <AlertTitle>Sucesso!</AlertTitle>
                            <AlertDescription>
                                Seu pedido foi processado com sucesso.
                            </AlertDescription>
                        </Alert>
                    ) : (
                        <Alert variant="destructive">
                            <AlertTriangle className="h-4 w-4" />
                            <AlertTitle>Itens Indisponíveis</AlertTitle>
                            <AlertDescription>
                                <div className="mt-2 space-y-2">
                                    <p className="text-sm">{orderResponseErro}</p>
                                </div>
                            </AlertDescription>
                        </Alert>
                    )}
                    </div>
                    
                    <Button onClick={fecharAlertaStatusPedido} className="w-full">
                        Fechar
                    </Button>
                </DialogContent>
            </Dialog>
        </div>
    )
}